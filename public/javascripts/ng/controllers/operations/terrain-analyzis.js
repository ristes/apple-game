Game.controller('TerrainAnalysisController', [
    '$scope',
    '$translate',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$weather',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $weather) {

      $scope.visible = false;
      $scope.url = "/public/images/game/operations/terrain-analysis.png";
      $scope.haveAnalysis = false;
      $scope.hide = function() {
        $scope.visible = false;
      }

      $scope.analyse = function() {
        $scope.haveAnalysis = true;
      }

      var unreg = $scope.$root.$on('operation-terrainAnalysis', function(_s,
              oper) {
        $scope.$root.$emit("side-hide");
        $scope.visible = true;
      });

    }]);