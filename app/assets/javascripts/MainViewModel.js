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
            $.get('/dwellers/show/1').then(function(profileData) {
                var viewModel = new MetersViewModel(profileData, true);
                swapTemplate({
                    name: 'meters-template',
                    model: viewModel
                });
            });
        });

        this.get('#/meters', function() {
            $.get('/dwellers/show/1').then(function(profileData) {
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

        this.get('#/history', function () {
            $.get('/history/byFlatId/1').then(function (historyGroups) {
                var viewModel = new MeasurementsHistoryViewModel(historyGroups);
                swapTemplate({
                    name: 'measurements-history-template',
                    model: viewModel
                })
            })
        });

        this.get('#/allDwellers', function () {
            $.get('/dwellers/listAll').then(function (historyGroups) {
                var viewModel = new ListDwellersViewModel(historyGroups);
                swapTemplate({
                    name: 'list-dwellers-template',
                    model: viewModel
                })
            })
        });

        this.get('#/signUp', function () {
            var viewModel = new SignUpModel();
            swapTemplate({
                name: 'sign-up-template',
                model: viewModel
            });
        });

        this.get('admin#/request/:requestId', function() {
            var requestId = this.params['requestId'];
            $.when($.get('/api/request/'+requestId),
                $.get('/api/visited/'+requestId),
                $.get('/api/commented/'+requestId))
             .then(function(requestModel, visits, comments) {
                var viewModel = new RequestViewModel(requestModel[0], visits[0], comments[0]);
                swapTemplate({
                    name:'request-template',
                    model: viewModel
                });
            });
        });
    });

    this.run = function(startUrl) {
        app.run(startUrl);
    };
    $(".nav a").on("click", function(){
        $(".nav").find(".active").removeClass("active");
        $(this).parent().addClass("active");
    });

    ko.applyBindings(self, $('#main')[0]);
};