app.controller("cartController",function ($scope,$http,cartService,addressService) {
    $scope.findCartList=function(){
        cartService.findCartList().success(
            function(response){
                $scope.cartList=response;
                $scope.totalValue=cartService.sum($scope.cartList);
            }
        );
    }
    $scope.addGoodsToCartList=function(itemId,num){
        cartService.addGoodsToCartList(itemId,num).success(
            function (response) {
                if(response.success){
                    $scope.findCartList();
                }else{
                    alert(response.message);
                }
            }
        )
    }

    $scope.findAddressList=function(){
        cartService.findAddressList().success(
            function (response) {
                $scope.addressList=response;
                for(var i=0;i<$scope.addressList.length;i++){
                    if($scope.addressList[i].isDefault=='1'){
                        $scope.address = $scope.addressList[i];
                        break;
                    }
                }
            }
        )
    }
    $scope.selectAddress = function (address) {
        $scope.address=address;
    };

    $scope.isSelectAddress = function (address) {
        if (address == $scope.address) {
            return true;
        }else{
            return false;
        }
    };

    //支付方式
    $scope.order={"paymentType":1};
    $scope.selectPayType = function (type) {
        $scope.order.paymentType=type;
    };

    //查询实体
    $scope.findOne=function(id){
        addressService.findOne(id).success(
            function(response){
                $scope.entity= response;
            }
        );
    }

    //保存
    $scope.save=function(){
        var serviceObject;//服务层对象
        if($scope.entity.id!=null){//如果有ID
            serviceObject=addressService.update( $scope.entity ); //修改
        }else{
            serviceObject=addressService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    //重新查询
                    location.href = "getOrderInfo.html";//重新加载
                }else{
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele=function(){
        //获取选中的复选框
        addressService.dele( $scope.selectIds ).success(
            function(response){
                if(response.success){
                    $scope.reloadList();//刷新列表
                    $scope.selectIds=[];
                }
            }
        );
    }
    $scope.submitOrder=function(){
        $scope.order.receiverAreaName=$scope.address.address;
        $scope.order.receiverMobile=$scope.address.mobile;
        $scope.order.receiver=$scope.address.contact;
        cartService.submitOrder($scope.order).success(
            function (response) {
                if(response.success){
                    if($scope.order.paymentType=='1'){
                        location.href = "pay.html";
                    }else{
                        location.href = "paysuccess.html";
                    }
                }else{
                    alert(response.message);
                }
            }
        );
    }
})