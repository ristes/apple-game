Game.controller('DiggingController', [
    '$scope',
    '$translate',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$weather',
    '$plowing',
    '$day',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $weather, $plowing, $day) {

      var unreg = $scope.$root.$on('operation-digging', function(_s, oper) {

        $scope.$root.$emit('show-progress-global', {
          title: 'progress.digging',
          duration: 5
        });
        var res = $http.get("/LandTreatmanController/digging");
        res.success(function(data) {
          $day.load(data.farmer);
        });
      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);