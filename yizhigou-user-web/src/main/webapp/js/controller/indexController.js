app.controller("indexController",function ($http,$scope,loginService) {
    $scope.showName=function(){
        loginService.showName().success(
            function (response) {
                $scope.loginName=response.loginName;
            }
        )
    }
})