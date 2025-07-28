package com.yeobi.mall;

import java.util.List;

public class JavaProgramming {

  class CategoryFlatDto {

    Long id;
    String name;
    Long parentId;
  }

  class CatergoryTreeDto {

    Long id;
    String name;
    List<CatergoryTreeDto> children;
  }

  public static void main(String[] args) {

  }
}
