package com.pub.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.pub.api.exception.RequisicaoInvalidaException;
import com.pub.api.exceptionhandler.Problema.Objeto;
import com.pub.domain.exception.EntidadeNaoEncontradaException;
import com.pub.domain.exception.ObjetoJaCadastradoException;
import com.pub.domain.exception.ViolacaoRegraNegocioException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o problema persistir,"
			+ " entre em contato com o administrador do sistema.";
	
	private final MessageSource messageSource;
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		Throwable causaRaiz = ExceptionUtils.getRootCause(ex);
		
		if(causaRaiz instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) causaRaiz, headers, status, request);
		} else if(causaRaiz instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) causaRaiz, headers, status, request);
		}
		
		TipoProblema tipoProblema = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		
		String detalhe = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		
		Problema problema = criarProblemaBuilder(status, tipoProblema, detalhe)
				.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	@ExceptionHandler(ObjetoJaCadastradoException.class)
	public ResponseEntity<Object> handleObjetoJaCadastrado(ObjetoJaCadastradoException excecao, WebRequest request) {
		
		HttpStatus httpStatus = HttpStatus.CONFLICT;
		TipoProblema tipoProblema = TipoProblema.RECURSO_JA_CADASTRADO;
		String detalhe = excecao.getMessage();
		
		Problema problema = criarProblemaBuilder(httpStatus, tipoProblema, detalhe)
				.mensagemUsuario(excecao.getMessage())
				.build();
		
		return handleExceptionInternal(excecao, problema, new HttpHeaders(), httpStatus, request);
	}
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {
		
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		
		Problema problema = criarProblemaBuilder(httpStatus, TipoProblema.RECURSO_NAO_ENCONTRADO, ex.getMessage())
				.mensagemUsuario(ex.getMessage())
				.build();
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), httpStatus, request);
	}
	
	@ExceptionHandler({ ViolacaoRegraNegocioException.class})
	public ResponseEntity<Object> handleViolacaoRegraNegocio(ViolacaoRegraNegocioException ex, WebRequest request) {
		
		HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		
		Problema problema = criarProblemaBuilder(httpStatus, TipoProblema.VIOLACAO_REGRA_NEGOCIO, ex.getMessage())
				.mensagemUsuario(ex.getMessage())
				.build();
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), httpStatus, request);
	}
	
	@ExceptionHandler(RequisicaoInvalidaException.class) 
	public ResponseEntity<Object> handleRequisicaoInvalida(RequisicaoInvalidaException ex, WebRequest request) {
		
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		
		Problema problema = criarProblemaBuilder(httpStatus, TipoProblema.DADOS_INVALIDOS, ex.getMessage())
				.mensagemUsuario(ex.getMessage())
				.build();
		
		return handleExceptionInternal(ex, problema,  new HttpHeaders(), httpStatus, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		
		if(body == null) {
			body = Problema.builder()
					.status(statusCode.value())
					.titulo(HttpStatus.valueOf(statusCode.value()).getReasonPhrase())
					.timestamp(OffsetDateTime.now())
					.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		} else if(body instanceof String) {
			body = Problema.builder()
					.status(statusCode.value())
					.titulo((String) body)
					.timestamp(OffsetDateTime.now())
					.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, statusCode, request);
	}
	
	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException rootCause, HttpHeaders headers, HttpStatusCode httpStatusCode, WebRequest request) {
		
		String path = joinPath(rootCause.getPath());
		
		String detalhe = String.format("A propriedade %s não existe no tipo %s. Corrija ou remova essa propriedade e tente novamente",
				path, rootCause.getReferringClass().getSimpleName());
		
		TipoProblema tipoProblema = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		
		Problema problema = criarProblemaBuilder(httpStatusCode, tipoProblema, detalhe)
				.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(rootCause, problema, headers, httpStatusCode, request);
	}
	
	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders httpHeaders, HttpStatusCode httpStatusCode, WebRequest request) {
		
		String path = joinPath(ex.getPath());
		
		TipoProblema tipoProblema = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		
		String detalhe = String.format("A propriedade %s recebeu o valor %s, que é de um tipo inválido. Corrija e informe um valor compátivel com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		
		Problema problema = criarProblemaBuilder(httpStatusCode, tipoProblema, detalhe)
				.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problema, httpHeaders, httpStatusCode, request);
	}
	
	private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult result, HttpHeaders headers, HttpStatusCode httpStatus, WebRequest webRequest) {
		TipoProblema tipoProblema = TipoProblema.DADOS_INVALIDOS;
		
		String detalhe = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
		
		List<Objeto> objetos = result.getAllErrors().stream()
		         .map(objectError -> {
		        	 String mensagem = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
		        	 
		        	 String nome = objectError.getObjectName();
		        	 
		        	 if(objectError instanceof FieldError) {
		        		 nome = ((FieldError) objectError).getField();
		        	 }
		        	 
		        	 return Problema.Objeto.builder()
		        			 .nome(nome)
		        			 .mensagemUsuario(mensagem)
		        			 .build();
		         }).toList();
		
		Problema problema = criarProblemaBuilder(httpStatus, tipoProblema, detalhe)
				.mensagemUsuario(detalhe)
				.objetos(objetos)
				.build();
		
		return handleExceptionInternal(ex, problema, headers, httpStatus, webRequest);
	}
	
	private Problema.ProblemaBuilder criarProblemaBuilder(HttpStatusCode status, TipoProblema tipoProblema, String detalhe) {
		return Problema.builder()
				.status(status.value())
				.tipo(tipoProblema.getUri())
				.titulo(tipoProblema.getTitulo())
				.detalhe(detalhe)
				.timestamp(OffsetDateTime.now());
	}
	
	private String joinPath(List<Reference> references) {
		return references.stream()
				.map(ref -> ref.getFieldName())
				.collect(Collectors.joining("."));
	}
}
