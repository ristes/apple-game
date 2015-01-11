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
          var old = scope.value;
          scope.value = ui.value;

          if (scope.cfg.onChange && typeof scope.cfg.onChange === 'function') {
            scope.cfg.onChange(ui.value, old);
          }
        });
      }

      scope.cfg = scope.cfg || {
        range: "max",
        min: 0,
        max: 100,
        slide: slide
      };

      scope.cfg.slide = scope.cfg.slide || slide;

      scope.cfg.value = scope.value;

      scope.$watch('value', function(n, o) {
        if (n == o) { return; }
        $(element).slider("value", n);
      });

      scope.$watch('cfg', function(n, o) {
        if (n == o) { return; }
        $(element).slider(n);
      });

      $(element).slider(scope.cfg);
    }
  };

}])