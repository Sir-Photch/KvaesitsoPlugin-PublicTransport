<img src="app/src/main/res/mipmap-xxhdpi/ic_launcher_round.png"/>

# Public transport plugin for Kvaesitso

This plugin enables querying your local public transport providers for stops and departures near you, while using your favorite launcher app [Kvaesitso](https://github.com/MM2-0/Kvaesitso).
It is a thin wrapper around [schildbach/public-transport-enabler](https://github.com/schildbach/public-transport-enabler) and integrates its providers into Kvaesitso's location search.

## Usage

[<img src="https://github.com/machiav3lli/oandbackupx/blob/034b226cea5c1b30eb4f6a6f313e4dadcbb0ece4/badge_github.png"
    alt="Get it on GitHub"
    height="80">](https://github.com/Sir-Photch/KvaesitsoPlugin-PublicTransport/releases)

Download the .apk from the release page of this repository and enable it in the plugin settings. Then, go to the settings of the plugin (gears on the top right) when you have opened the settings of the plugin. Now, select the providers you want to get search results for. Note that if you activate multiple providers whose regions overlap, you might get duplicate results. Then, go back to your homescreen and start searching! (Make sure you have location search enabled in your filterbar and Kvaesitso has location permissions.)

### Video demo:
<details>
  <summary>Expand</summary>

  [plugin-usage.webm](https://github.com/Sir-Photch/KvaesitsoPlugin-PublicTransport/assets/47949835/724fcde6-c758-4e75-8725-6b8d9a3813e3)
</details>



## Translation

You can translate this plugin using [Weblate](https://hosted.weblate.org/projects/kvaesitsoplugin-publictransport/localization/).

<a href="https://hosted.weblate.org/engage/kvaesitsoplugin-publictransport/">
<img src="https://hosted.weblate.org/widget/kvaesitsoplugin-publictransport/localization/287x66-grey.png" alt="Übersetzungsstatus" />
</a>

## Providers

Currently, all providers of [schildbach/public-transport-enabler](https://github.com/schildbach/public-transport-enabler) that do _not_ require an API key are accessible through this plugin. Generally, if you miss one for your area, check [schildbach/public-transport-enabler](https://github.com/schildbach/public-transport-enabler) whether or not it is available there, and if it is, whether or not it requires an API key. As of right now, there is no support for providers that require an API key.

## Roadmap

This is a list of things that are TODO:

- [ ] Add support for Providers that require an API key
- [ ] Add icons to location attribution
- [ ] Redesign settings screen (sometime)

## License

This plugin is licensed under the GNU General Public License v3:
```
Kvaesitso Public Transport Plugin
Copyright (C) 2024  Sir-Photch and contributors

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```



