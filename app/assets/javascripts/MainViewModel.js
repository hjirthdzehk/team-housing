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
        this.get('#/measuresInput', function() {
            $.get('/meters/byFlatId/1').then(function(groups) {
                var viewModel = new MeasuresInputViewModel(groups);
                swapTemplate({
                    name: 'measures-input-template',
                    model: viewModel
                });
            });
        });

        this.get('#/profile', function() {
            $.get('/dwellers/1').then(function(profileData) {
                var viewModel = new ProfileViewModel(profileData);
                swapTemplate({
                    name: 'profile-template',
                    model: viewModel
                });
            });
        })
    });

    this.run = function() {
        app.run('#/measuresInput');
    };
    ko.applyBindings(self, $('#main')[0]);
};
