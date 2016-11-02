var MainViewModel = function() {
    var self = this;

    this.template = ko.observable({});
    var swapTemplate = function(template) {
        if (self.template().model && _.isFunction(self.template().model.dispose)) {
            self.template().model.dispose();
        }

        self.template(template);
    };

    var app = Sammy('#main', function() {
        this.get('#/readings/submit', function() {
            $.get('/dwellers/1').then(function(profileData) {
                var viewModel = new MetersViewModel(profileData, true);
                swapTemplate({
                    name: 'meters-template',
                    model: viewModel
                });
            });
        });

        this.get('#/meters', function() {
            $.get('/dwellers/1').then(function(profileData) {
                var viewModel = new MetersViewModel(profileData, false);
                swapTemplate({
                    name: 'meters-template',
                    model: viewModel
                });
            });
        });

        this.get('#/meters/statistics', function() {
            var viewModel = new MetersStatisticsViewModel();
            swapTemplate({
                name:'meters-statistic-template',
                model: viewModel
            });
        });
    });

    this.run = function() {
        app.run('#/measuresInput');
    };
    ko.applyBindings(self, $('#main')[0]);
};
