package com.ywzs.Dto;

import com.ywzs.entity.Setmeal;
import com.ywzs.entity.SetmealDish;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SetMealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
