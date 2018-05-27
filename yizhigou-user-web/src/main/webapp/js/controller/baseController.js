app.controller("baseController",function($scope){
    //重新加载列表数据
    $scope.reloadList=function(){
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    }

    //分页空间配置
    $scope.paginationConf={
        currentPage:1,
        totalItems:10,
        itemsPerPage:10,
        perPageOptions:[10,20,30,40,50],
        onChange:function () {
            $scope.reloadList(); //重新加载
        }
    }
    //定义一个全局变量，用于存储删除的id
    $scope.selectIds=[];
    //获取被选中的数据的id
    $scope.updateSelection=function($event,id){
        if($event.target.checked){
            $scope.selectIds.push(id);
        }else{
            var idx=$scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx,1);
        }
    }

    //将Json转换为字符窜
    $scope.jsonToString=function(jsonString,key){
        var json=JSON.parse(jsonString);
        var value="";
        for(var i=0;i<json.length;i++){
            if(i>0){
                value+=",";
            }
            value+=json[i][key];
        }
        return value;
    }
})