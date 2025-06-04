package com.wang.bss.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    /**
     * 成功的响应方法，返回带有数据的响应
     *
     * @param data 返回的数据
     * @param <E> 泛型
     * @return Result
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功！", data);
    }

    /**
     * 成功的响应方法，不带数据
     *
     * @return Result
     */
    public static Result<Void> success() {
        return new Result<>(0, "操作成功", null);
    }

    /**
     * 错误的响应方法
     *
     * @param message 错误信息
     * @return Result
     */
    public static Result error(String message) {
        return new Result<>(1, message, null);
    }
}
