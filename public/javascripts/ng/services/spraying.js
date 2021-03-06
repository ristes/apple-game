Game.factory('Spraying', [
    'BoughtItems',
    '$day',
    '$http',
    'jQuery',
    function(BoughtItems, $day, $http, $) {
        return {
        	avail: function(callback) {
        		
        	},
            spray: function(item) {
                $http({
                    method: 'POST',
                    url: "/sprayingcontroller/spray",
                    data: $.param({
                        itemid: item.id
                    }),
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }).then(function(res) {
                    if (res.farmer && res.farmer.balans) {
                        $day.load(res);
                    }
                    BoughtItems.load();
                });
            }
        }
    }
]);
