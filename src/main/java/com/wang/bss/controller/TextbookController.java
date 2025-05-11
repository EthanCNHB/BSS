package com.wang.bss.controller;

import com.wang.bss.pojo.Result;
import com.wang.bss.pojo.Textbook;
import com.wang.bss.service.TextbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/textbook")
public class TextbookController {

    @Autowired
    private TextbookService textbookService;

    @PostMapping
    public Result addTextbook(@RequestBody Textbook textbook){
        textbookService.add(textbook);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<String> searchTextbooks(@RequestParam Map<String, Object> params) {

        return Result.success("所有教材数据");
    }

    @PutMapping
    public Result updateTextbook(@RequestBody @Validated Textbook textbook) {
        textbookService.updateTextbook(textbook);
        return Result.success();
    }

    @DeleteMapping("/{textbookId}")
    public Result deleteTextbook(@PathVariable Integer textbookId) {
        textbookService.deleteTextbook(textbookId);
        return Result.success();
    }
}
