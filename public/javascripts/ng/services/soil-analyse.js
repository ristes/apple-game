Game.factory("SoilAnalyse", ['$rootScope', '$http',
    function($rootScope, $http) {
        return {
            load: function(data) {
                $http({
                    url: '/infotablecontroller/news'
                }).then(function(res) {
                    if (res.data.length > 0) {
                        $rootScope.soilAnalyse = data;
                    }
                })
            }
        }
    }
]);
