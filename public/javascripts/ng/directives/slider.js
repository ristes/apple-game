GameDirectives.directive('slider', ['jQuery', function($) {

  return {
    transclude: true,
    scope: {
      value: '=',
      cfg: '='
    },
    link: function(scope, element, attrs, ctrl, transclude, formCtrl) {

      function slide(event, ui) {
        scope.$apply(function() {
          scope.value = ui.value;
        });
      }

      scope.cfg = scope.cfg || {
        range: "max",
        min: 0,
        max: 25,
        slide: slide
      };

      scope.$watch('value', function(n, o) {
        if (n == o) { return; }
        $(element).slider("value", n);
      });

      $(element).slider(scope.cfg);
    }
  };

}])