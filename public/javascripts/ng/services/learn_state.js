Game.factory('LearnState',['$http', function($http) {
	return {
		save: function(item) {
			$http({
                method: 'POST',
                url: "/learnstatecontroller/done",
                data: $.param({'id':item.id}),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function(res) {
                
            });
			
//			$http.post('learnstatecontroller/done',{'id':item.id}).success(function(data) {
//				
//			});
		}
	}
}])