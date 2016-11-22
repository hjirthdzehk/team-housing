/**
 * Created by VladVin on 22.11.2016.
 */

var CreateRequestViewModel = function(flatId) {
    var self = this;
    this.flatId = flatId;

    self.description = ko.observable('');

    self.createRequest = function () {
        if (self.description !== '') {
            $.post('/api/requests/create/' + this.flatId, {
                'description'   :self.description()
            }).then(function () {
                self.description('');
            });
        }
    }
};
