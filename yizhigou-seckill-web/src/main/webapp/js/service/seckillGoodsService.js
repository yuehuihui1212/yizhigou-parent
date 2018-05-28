app.service("seckillGoodsService",function ($http) {
    this.findList=function(){
        return $http.get('seckillGoods/findList.do');
    }

    this.findOne=function(id){
        return $http.get('seckillGoods/findOneFromRedis.do?id=' + id);
    }

    this.submitOrder=function(id){
        return $http.get('seckillOrder/submitOrder.do?seckillId=' + id);
    }
})