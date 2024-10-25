package vn.hoidanit.jobhunter.util.error;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.naming.Binding;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.hoidanit.jobhunter.domain.RestResponse;

// Chú ý:  @RestControllerAdvice = @ControllerAdvice + @ResponseBody
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            BadCredentialsException.class,
            UsernameNotFoundException.class })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("Call API failed");
        res.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Xử lý lỗi khi validate dữ liệu
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationErorr(
            MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());
        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}
