Game.factory("InfoTable", [ '$rootScope', '$http', function($rootScope, $http) {
	return {
		setNews : function(res) {
			if (res && res.data && res.data.length > 0) {
				for (var i = 0; i < res.data.length; i++) {
					$rootScope.$emit('info-show', {
						infos : res.data,
						showNext : true,
						titleImageUrl : '/public/images/game/jabolko.png'
					});
				}
			}
		}
	}
}]);
