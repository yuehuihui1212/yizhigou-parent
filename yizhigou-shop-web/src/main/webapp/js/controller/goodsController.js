 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location ,uploadService,goodsService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){
		var id=$location.search()['id'];
		if(id==null){
			return ;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				editor.html($scope.entity.goodsDesc.introduction);
                $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
                $scope.entity.itemList=response.itemList;
                for(var i=0;i<$scope.entity.itemList.length;i++){
                    $scope.entity.itemList[i].spec=JSON.parse($scope.entity.itemList[i].spec);
				}
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			$scope.entity.goodsDesc.introduction=editor.html();
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
					$scope.entity={};
					editor.html('');
                    location.href = "goods.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    //上传图片
	$scope.uploadFile=function(){
		uploadService.uploadFile().success(
			function(response){
				if(response.success){
					$scope.image_entity.url=response.message;
				}else{
                    alert(response.message);
				}
			}
		).error(function(){
			alert("上传图片失败");
		})
	}
	$scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
	$scope.add_image_entity=function(){
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
		$scope.image_entity={};
	}
	$scope.dele_image_entity=function(index){
		$scope.entity.goodsDesc.itemImages.splice(index,1);
	}
	$scope.dele_image=function(){
		for(var i=0;i<$scope.selectIds.length;i++){
            //var number = $scope.entity.goodsDesc.itemImages.indexOf($scope.selectIds[i]);
            $scope.entity.goodsDesc.itemImages.splice($scope.selectIds[i],1);
		}
	}
	//一级分类查询
	$scope.selectItemCat1List=function(){
		itemCatService.findByParentId(0).success(
			function(response){
				$scope.itemCat1List=response;
			}
		)
	}
    //二级分类查询
	$scope.$watch("entity.goods.category1Id",function(newValue,oldValue){
		itemCatService.findByParentId(newValue).success(
			function(response){
				$scope.itemCat2List=response;
			}
		)
	})
    //三级分类查询
	$scope.$watch("entity.goods.category2Id",function(newValue,oldValue){
		itemCatService.findByParentId(newValue).success(
			function(response){
				$scope.itemCat3List=response;
			}
		)
	})
	//查询模板ID
	$scope.$watch("entity.goods.category3Id",function(newValue,oldValue){
		itemCatService.findOne(newValue).success(
			function(response){
				$scope.entity.goods.typeTemplateId=response.typeId;
			}
		)
	})
	//查询品牌
	$scope.$watch("entity.goods.typeTemplateId",function (newValue,oldValue) {
		typeTemplateService.findOne(newValue).success(
			function(response){
				$scope.typeTemplate=response;
				$scope.typeTemplate.brandIds=JSON.parse($scope.typeTemplate.brandIds);
                if($location.search()['id']==null){
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
                }
			}
		)
		typeTemplateService.findSpecList(newValue).success(
			function(response){
				$scope.specList=response;
			}
		)
    })
	//修改规格项是否被选中
	$scope.updateSpecAttribute=function($event,name,value){
		var Object=$scope.selectObjectByKey($scope.entity.goodsDesc.specificationItems,"attributeName",name);
		if(Object!=null){
			if($event.target.checked){
				Object.attributeValue.push(value);
			}else{
				Object.attributeValue.splice(Object.attributeValue.indexOf(value),1);
				if(Object.attributeValue.length==0){
					$scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(Object),1);
				}
			}
		}else{
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}
	}

	$scope.createItemList=function(){
		$scope.entity.itemList=[{spec:{},price:0,num:999,status:'0',isDefault:'0'}]
		if($location.search()['id']==null){
            var items=$scope.entity.goodsDesc.specificationItems;
        }
		for(var i=0;i<items.length;i++){
			$scope.entity.itemList=$scope.addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}
	}
	$scope.addColumn=function(list,columnName,columnValue){
		var newList=[];
		for(var i=0;i<list.length;i++){
			var oldRow=list[i];
			for(var j=0;j<columnValue.length;j++){
				var newRow=JSON.parse(JSON.stringify(oldRow));
				newRow.spec[columnName]=columnValue[j];
				newList.push(newRow);
			}
		}
		return newList;
	}

	$scope.status=['申请中','审核未通过','审核通过','关闭']
	$scope.categoryList=function(){
		itemCatService.findAll().success(
			function(response){
				for(var i=0;i<response.length;i++){
					$scope.categoryList[response[i].id]=response[i].name;
				}
			}
		)
	}

    $scope.checkAttributeValue=function(specName,optionName){
		var items=$scope.entity.goodsDesc.specificationItems;
		var Object=$scope.selectObjectByKey(items,"attributeName",specName);
		if(Object==null){
			return false;
		}else{
			if(Object.attributeValue.indexOf(optionName)>=0){
				return true;
			}else{
				return false;
			}
		}
	}
});	
