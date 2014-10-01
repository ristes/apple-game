Game.controller('ClothingController', ['$scope', function($scope) {

  var unreg = $scope.$root.$on('clothing-plowing', function(_s, oper) {

    $scope.$root.$emit('show-progress-global', {
      title: 'progress_clothing',
      duration: 20
    });
    // $plowing.plowing();
  });

  $scope.$on("$destroy", function() {
    if (unreg) {
      unreg();
    }
  });

}]);