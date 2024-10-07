package com.example.nagoyameshi.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewForm {

	@NotNull(message = "1~5段階で評価をお願いします。")
	@Min(value = 1, message = "スコアは最低1点です。")
	@Max(value = 5, message = "スコアは最高５点です。")
    private Integer score;
    
    @NotBlank(message = "コメントをお願いします。")
    private String content;
    
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
