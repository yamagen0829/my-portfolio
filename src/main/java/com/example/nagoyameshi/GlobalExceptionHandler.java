package com.example.nagoyameshi;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException e, Model model) {
        model.addAttribute("message", "リソースが見つかりませんでした");
        return "error/404";  // 404.htmlを表示するテンプレート名を返します
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("message", "サーバーエラーが発生しました");
        return "error/errorPage";  // error.htmlを表示するテンプレート名を返します
    }
}
