class LogMessage {
  String name;
  String level;
  String message;

  LogMessage({this.name, this.level, this.message});

  LogMessage.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    level = json['level'];
    message = json['message'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['name'] = this.name;
    data['level'] = this.level;
    data['message'] = this.message;
    return data;
  }
}