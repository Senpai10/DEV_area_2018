function trigger_action(action_id)
{
	if (action_id == 0)
	{
		var tag = $('#soloid').val();
		var record = $('#solocap').val();
		var action_info = {
			tag: tag,
			record: record
		}
	}
	else if (action_id == 1)
	{
		var tag = $('#mytag').val();
		var friend_tag = $('#friendtag').val();
		var action_info = {
			tag: tag,
			friend_tag: friend_tag
		}
	}
	else if (action_id == 2)
	{
		var clan_tag = $('#clan_tag').val();
		var score = $('#clan_cap').val();
		var action_info = {
			clan_tag: clan_tag,
			score: score
		}
	}
	else if (action_id == 3)
	{
		var symbol = $('#symbol').val();
		var value_cap = $('#valuecap').val();
		var action_info = {
			symbol : symbol,
			value_cap : value_cap
		}
	}
	else if (action_id == 4)
	{
		var repo = $('#repo').val();
		var owner = $('#owner').val();
		var action_info = {
			repo : repo,
			owner : owner
		}
	}
	else if (action_id == 5)
	{
		var channel = $('#channel').val();
		var action_info = {
			channel : channel,
		}
	}
	else if (action_id == 6)
	{
		var city = $('#cityhot').val();
		var temperature = $('#maxtemp').val();
		var action_info = {
			city : city,
			temperature : temperature
		}
	}
	else if (action_id == 7)
	{
		var city = $('#citycold').val();
		var temperature = $('#mintemp').val();
		var action_info = {
			city : city,
			temperature : temperature
		}
	}
	else if (action_id == 8)
	{
		var followers = $('#followers').val();
		var action_info = {
			followers : followers
		}
	}
	else if (action_id == 9)
	{
		var friendName = $('#friend_name').val();
		var action_info = {
			friendName : friendName
		}
	}
	else if (action_id == 10)
	{
		var channel = $('#ytchannel').val();
		var subscribers = $('#ytsub').val();
		var action_info = {
			channel : channel,
			subscribers : subscribers
		}
	}
	else if (action_id == 11)
	{
		var channel = $('#meytchannel').val();
		var friend_channel = $('#friendytchannel').val();
		var action_info = {
			channel : channel,
			friend_channel : friend_channel
		}
	}
	else if (action_id == 12)
	{
		var channel = $('#favochannel').val();
		var action_info = {
			channel : channel,
		}
	}
	else if (action_id == 13)
	{
		var repo = $('#ghaccount').val();
		var owner = $('#ghowner').val();
		var stars = $('#stars').val();
		var action_info = {
			repo : repo,
			owner : owner,
			stars : stars
		}
	}
	else if (action_id == 14)
	{
		var summoner = $('#playerlevel').val();
		var region = $('#regionlevel').val();
		var action_info = {
			summoner : summoner,
			region : region
		}
	}
	else if (action_id == 15)
	{
		var summoner = $('#playerbo').val();
		var region = $('#regionbo').val();
		var action_info = {
			summoner : summoner,
			region : region
		}
	}
	localStorage.setItem('myAction', JSON.stringify(action_info));
	localStorage.setItem('myAction_id', JSON.stringify(action_id));
}