Game.controller('PlantingStateController', [
    '$scope',
    '$location',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$plantation',
    '$weather',
    function($scope, $location, $http, Store, StoreItems, Operations, $farmer,
            $items, $plantation, $weather) {

      $weather.load();
      $plantation.load();

      $scope.actions = Operations['planting'];

    }]);