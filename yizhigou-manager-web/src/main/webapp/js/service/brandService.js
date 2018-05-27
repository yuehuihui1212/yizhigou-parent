app.service('brandService',function($http){
    //品牌全查
    this.findAll=function () {
        return $http.get('../brand/findAll.do');
    }
    //根据id单查
    this.findOne=function (id) {
        return $http.get("../brand/findOne.do?id="+id);
    }
    //分页
    this.findPage=function(page,rows){
        return $http.get("../brand/findPage.do?page="+page+"&rows="+rows);
    }
    //删除
    this.dele=function (ids) {
        return  $http.get("../brand/delete.do?ids="+ids);
    }
    //添加
    this.add=function(entity){
        return  $http.post('../brand/add.do',entity);
    }
    //修改
    this.update=function(entity){
        return $http.post('../brand/update.do',entity);
    }
    //查询
    this.search=function (page,rows,entity) {
        return $http.post("../brand/search.do?page="+page+"&rows="+rows,entity);
    }

    //用于下拉框添加品牌
    this.selectOptionList=function(){
        return $http.get("../brand/selectOptionList.do");
    }
})