Game.factory('Plantation', [
    'State',
    '$http',
    function(State, $http) {
        return {
            load: function(fn) {
                var res = $http.get("/AuthController/plantation");
                res.success(function(data) {
                    State.set("plantation", data);
                    if (fn && typeof fn === 'function') {
                        fn(data);
                    }
                });
            },
            save: function(array, seedlings, fn) {
                var res = $http.post("/PlantationController/savePlanting?array=" + array + '&seedlings=' + seedlings);
                res.success(function(data) {
                    if (fn && typeof fn === 'function') {
                        fn(data);
                    }
                })
            }
        };
    }
]);
