var config={_id:"rs0",members:[
        {_id:0,host:"mongo1:27017","votes":1 ,"priority":1},
        {_id:1,host:"mongo2:27017","votes":0,"priority":0 },
        {_id:2,host:"mongo3:27017","votes":0,"priority":0}]};
rs.initiate(config);
