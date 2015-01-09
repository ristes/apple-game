Game.factory('Harvesting', [ '$http', 'State', function($http, State) {
	return {
		shriber : function(callback) {
			var res = $http.get("/HarvestingController/harvestingPeriod");
			res.success(function(data) {
				if (typeof callback === 'function') {
					callback(data);
				}
			});
		},
		harvest : function(gameResult, callback) {

			var res = $http.get("/HarvestingController/harvest", {
				goodcollected : 1,
				goodtotal : 3,
				badcollected : 2,
				badtotal : 4,
				plantationseedling : 1
			});
			res.success(function(data) {
				if (data.status === true) {
					State.set('farmer', data.farmer);
					if (typeof callback === 'function') {
						callback();
					}
				}
			});
		}
	};
} ]);