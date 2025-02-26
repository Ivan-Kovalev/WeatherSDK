# Weather Data Collection Software Development Kit

## The service is immutable and can be used in two modes, which are selected during initialization:

Request - processing client requests, saving data to the cache and issuing weather data.

Polling - automatic updating of information stored in the cache to achieve zero delay in method execution.

## Usage examples:

1. Create an instance of the SDK object
> WeatherSDK sdkExampleRequest = new WeatherSDKImpl(ModeWeatherSDK.REQUEST, "_Your API key_");
2. We call the method for getting the weather, passing the city we are interested in as a parameter
> Weather example1 = sdkExampleRequest.getWeather(_Your city_);
3. We finish working with SDK by calling the shutdown method
> sdkExampleRequest.shutdown();

## Enjoy using it!