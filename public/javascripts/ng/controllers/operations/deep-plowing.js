Game.controller('DeepPlowingController', [
    '$scope',
    'Plowing',
    'Toaster',
    function($scope, Plowing, Toaster) {

      var unreg = $scope.$root.$on('operation-deep-plowing',
              function(_s, oper) {

//                $scope.$root.$emit('show-progress-global', {
//                  title: 'progress_deep_plowing',
//                  duration: 10,
//                  actionToShare:'DeepPlowing'
//                });
                Plowing.deepPlowing();
                Toaster.success("Plowing is done! Great job!");
                
              });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);