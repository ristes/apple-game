Game.controller('HarvestingController', [
    '$scope',
    '$translate',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$weather',
    '$day',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $weather, $day) {

      var unreg = $scope.$root.$on('operation-harvest', function(_s, oper) {

        $scope.$root.$emit('show-progress-global', {
          title: 'progress.harvest',
          duration: oper.duration
        });
        var res = $http.get("/HarvestingController/harvest");
        res.success(function(data) {
          $day.load(data.farmer);
          var res1 = $http.get("/salecontroller/sale?quantity="
                  + data.farmer.apples_in_stock);
          res.success(function(data) {
            $day.load(data.farmer);
          });
        });
      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);