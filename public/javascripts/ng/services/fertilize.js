Game.factory('Fertilize', ['State', '$http', function(State, $http) {
    return {
        fertilize: function(fertilizer, callback) {
            $http({
                method: 'POST',
                url: "/fertilizationcontroller/fertilize",
                data: $.param(fertilizer),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function(res) {
                if (res.data && res.data.status == true && res.data.farmer) {
                    State.set('farmer', res.data.farmer);
                    if (typeof callback === 'function') {
                        callback();
                    }
                }
            });
        }
    }
}]);
