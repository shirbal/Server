package com.taru.model;

import com.taru.model.enums.CategoryLogic;

/**
 * Created by Shiran Maor on 10/31/2015.
 */
public class CategoryProperties {

  private String _categoryName;
  private CategoryLogic _categoryLogic;

  public String getCategoryName() {
    return _categoryName;
  }

  public void setCategoryName(String categoryName) {
    _categoryName = categoryName;
  }

  public CategoryLogic getCategoryLogic() {
    return _categoryLogic;
  }

  public void setCategoryLogic(CategoryLogic categoryLogic) {
    _categoryLogic = categoryLogic;
  }
}
