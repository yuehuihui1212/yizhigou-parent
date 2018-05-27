var app=angular.module("yizhigou",[]);
app.filter("trustHtml",['$sce',function ($sce) {
        return function(data){
            return $sce.trustAsHtml(data);
        }
}])