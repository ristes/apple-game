Game.factory('Spraying', [
    'BoughtItems',
    '$day',
    '$http',
    'jQuery',
    function(BoughtItems, $day, $http, $) {
        return {
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
                    if (res.data && res.data.balans) {
                        $day.load(res.data);
                    }
                    BoughtItems.load();
                });
            }
        }
    }
]);
