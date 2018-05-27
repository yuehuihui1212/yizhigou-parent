app.controller("indexController",function($scope,$http,loginService){
    $scope.showLoginName=function(){
        loginService.loginName().success(
            function (response) {
            $scope.loginName=response.loginName;
        })
    }
})