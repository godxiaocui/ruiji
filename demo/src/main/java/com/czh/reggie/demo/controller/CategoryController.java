package com.czh.reggie.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czh.reggie.demo.common.BaseContext;
import com.czh.reggie.demo.common.R;
import com.czh.reggie.demo.pojo.Category;
import com.czh.reggie.demo.pojo.Employee;
import com.czh.reggie.demo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理分类
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增品类
     *
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("sucess");
    }

    /**
     * 分页查询
     * 1, 构造分页构造器
     * 2. 条件构造器
     *  a. 过滤条件
     *  b, 排序条件
     *3.运行一下分页查询
*/
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize){
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.orderByDesc(Category::getSort);
        categoryService.page(pageInfo,categoryLambdaQueryWrapper);
        return R.success(pageInfo);
    }
    /**
     * 删除注意，传过来不是rest风格的。所以不能用
     *     @DeleteMapping("{ids}")
     *     public R<String> delete( @PathVariable Long ids){
     * http://localhost:8080/category?ids=1653754370296168449
     */
    @DeleteMapping()
    public R<String> delete(  Long ids){
        categoryService.remove(ids);
        return  R.success("分类信息删除成功");
    }

    /**
     * 修改，json格式的响应提都要是
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
}
