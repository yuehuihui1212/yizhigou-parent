app.controller("seckillGoodsController",function ($scope,$http,$location,$interval,seckillGoodsService) {
    $scope.findList=function(){
        seckillGoodsService.findList().success(
            function (response) {
                $scope.list=response;
            }
        );
    }
    $scope.findOne=function(){
        seckillGoodsService.findOne($location.search()['id']).success(
            function(response){
                $scope.entity=response;
                allsecond=Math.floor((new Date($scope.entity.endTime).getTime()-new Date().getTime())/1000);
                time=$interval(function () {
                    allsecond=allsecond-1;
                    $scope.timeString=convertTimeString(allsecond);
                    if(allsecond<=0){
                        $interval.cancel(time);
                    }
                },1000);
            }
        )
    }
    convertTimeString=function(allsecond){
        var days=Math.floor(allsecond/(60*60*24));
        var hours=Math.floor((allsecond-days*60*60*24)/3600);
        var minutes=Math.floor((allsecond-days*60*60*24-hours*60*60)/60);
        var seconds=allsecond -days*60*60*24 - hours*60*60 -minutes*60;
        var timeString="";
        if(days>0){
            timeString=days+"天";
        }
        return timeString+hours+":"+minutes+":"+seconds;
    }

    $scope.seckillOrder=function () {
        seckillGoodsService.submitOrder($scope.entity.id).success(
            function(response){
                if(response.success){
                    alert("下单了请尽快去支付，否则可能会自动无效订单呀");
                    location.href = "pay.html";
                }else{
                    alert(response.message);
                    alert($scope.entity.id);
                    location.href = "index.html#?iid="+$scope.entity.id;
                }
            }
        )
    }

    $scope.goto=function(){
        alert($location.search()['iid']);
        location.href="seckill-item.html#?id="+$location.search()['iid'];
    }
})