app.controller('searchController',function($scope,$http,searchService,$location){
    $scope.loadkeywords=function(){
        $scope.searchMap.keywords = $location.search()['keywords'];
        $scope.search();
    }
    $scope.search=function(){
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);
        searchService.search($scope.searchMap).success(
            function(response){
                $scope.resultMap=response;
                $scope.buildPageLabel();
            }
        )
    }
    $scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};
    $scope.addSearchItem=function(key,value){
        if(key=='category'||key=='brand'|| key=='price'){
            $scope.searchMap[key]=value;
        }else{
            $scope.searchMap.spec[key]=value;
        }
        $scope.search();
    }
    $scope.removeSearchItem=function(key){
        if(key=='category'||key=='brand'||key=='price'){
            $scope.searchMap[key]='';
        }else{
           delete  $scope.searchMap.spec[key];
        }
        $scope.search();
    }

    $scope.priceList=['0-500','500-1000','1000-1500','1500-2000','2000-3000','3000-*'];

    //分页
    $scope.buildPageLabel=function(){
        $scope.pageLabel = [];
        var maxPageNo=$scope.resultMap.totalPages;
        var firstPage=1;
        var lastPage=maxPageNo;
        $scope.firstDot=true;
        $scope.lastDot=true;
        if($scope.resultMap.totalPages>7){
            if($scope.searchMap.pageNo<=5){
                lastPage=7;
                $scope.firstDot=false;
            }else if($scope.searchMap.pageNo>=$scope.resultMap.totalPages-2){
                lastPage=$scope.resultMap.totalPages;
                $scope.lastDot=false;
            }else{
                firstPage=$scope.searchMap.pageNo-2;
                lastPage=$scope.searchMap.pageNo+2;
            }
        }else{
            $scope.firstDot=false;
            $scope.lastDot=false;
        }

        for(var i=firstPage;i<=lastPage;i++){
            $scope.pageLabel.push(i);
        }
    }
    $scope.queryPage=function(pageNo){
        if(pageNo<0 || pageNo>$scope.resultMap.totalPages){
            return;
        }
        $scope.searchMap.pageNo=pageNo;
        $scope.search();
    }
    $scope.isTopPage=function(){
        if ($scope.searchMap.pageNo == 1) {
            return true;
        }else{
            return false;
        }
    }
    $scope.isEndPage=function(){
        if ($scope.searchMap.pageNo==$scope.resultMap.totalPages) {
            return true;
        }else{
            return false;
        }
    }
    $scope.sortSearch = function (sort, sortField) {
        $scope.searchMap.sort=sort;
        $scope.searchMap.sortField=sortField;
        $scope.search();
    };
    $scope.keywordsIsBrand=function(){
        for(var i=0;i<$scope.resultMap.brandList.length;i++){
            if($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
                return true;
            }
        }
        return false;
    }
})