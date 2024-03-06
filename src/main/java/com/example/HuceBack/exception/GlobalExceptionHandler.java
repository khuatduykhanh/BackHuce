package com.example.HuceBack.exception;

import com.example.HuceBack.dto.error.ErrorDetail;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {  // để tuỳ chỉnh phản hồi validation
    // trong PostDto chúng ta sẽ phải kế thừa từ lớp ResponseEntityExceptionHandler

    // xử lý ngoại lệ cho ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class) // bắn ra ngoại lệ ResourceNotFoundException với form ErrorDetail
    public ResponseEntity<ErrorDetail> handlerResourceNotFoundException(ResourceNotFoundException e, WebRequest w){
        ErrorDetail err = new ErrorDetail(new Date(), e.getMessage(),w.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class) // bắn ra ngoại lệ ResourceAlreadyExistException với form ErrorDetail
    public ResponseEntity<ErrorDetail> handlerResourceAlreadyExistException(ResourceAlreadyExistException e, WebRequest w){
        ErrorDetail err = new ErrorDetail(new Date(), e.getMessage(),w.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
    //xử lý ngoại lệ cho BlogAPIException
    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorDetail>  handlerBlogAPIException(APIException e, WebRequest w){
        ErrorDetail err = new ErrorDetail(new Date(), e.getMessage(),w.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    // xử lý ngoại lệ chung cho toàn bộ hệ thống, tất cả ngoại lệ điều kế thừa Exception
    // xử lý ngoại lệ cho Exception là xử lý ngoai lệ cho toàn bộ hệ
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail>  handlerGolbalException(Exception e, WebRequest w){
        ErrorDetail err = new ErrorDetail(new Date(), e.getMessage(),w.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
    @Override // ghi đè phương thức handleMethodArgumentNotValid trong ResponseEntityExceptionHandler để tuỳ chỉnh phản hồi validation
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String,String > err = new HashMap<>(); // sử dụng Map để lưu trữ tên miền với message lỗi
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField(); // trả về field name bị lỗi
            String message = error.getDefaultMessage(); // trả về message lỗi của field name
            err.put(fieldName,message);
        });
        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    } // MethodArgumentNotValidException đối tượng này có các phương thức để lấy được lỗi của validation

}
