package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除之前需要判断
     * @param id
     */
    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询

        queryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(queryWrapper);

        //查询是否关联了菜品，如果已经关联了，直接抛出业务异常

        if(count>0) {

            throw new CustomException("当前分类下关联了菜品， 不能删除");

        }


        //查询是否关联了套餐，如果已经关联了，直接抛出业务异常
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId, id);

        int count1 = setmealService.count(queryWrapper1);

        if( count1>0){
            //已经关联套餐
            throw new CustomException("当前分类下关联了套餐， 不能删除");

        }

        super.removeById(id);
    }
}
