app.controller('brandController',function($scope,$http,$controller,brandService){
    $controller("baseController",{$scope:$scope})
    $scope.findAll=function(){
        brandService.findAll().success(
            function(response){
                $scope.list=response;
        })
    }
    //分页查询
    $scope.findPage=function(page,rows){
    brandService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;

            })
    }
    //添加或修改
    $scope.save=function(){
        var obj=brandService.add($scope.entity);
        if($scope.entity.id!=null){
            obj=brandService.update($scope.entity);
        }
        obj.success(
            function (response) {
                if(response.success){
                    $scope.reloadList();
                }else{
                    alert(response.message);
                }
            }
        )
    }

    //根据ID单查
    $scope.findOne=function (id) {
        brandService.findOne(id).success(
            function(response){
                $scope.entity=response;
            });
    }

    //删除
    $scope.dele=function(){
        brandService.dele($scope.selectIds) .success(
            function (response) {
                if(response.success){
                    $scope.reloadList();
                }
            }
        )
    }
    //查询
    $scope.searchEntity={};
    $scope.search=function (page,rows){
        brandService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;
            }
        )
    }
})