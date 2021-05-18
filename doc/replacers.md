#Replacers
Replacers are the processors that will replace blocks in your structure. These are meant to complement the vanilla processors. If you are simply replacing one block for another, consider using `minecraft:rule`, instead of any of these replacers. 

[Read more about Random Type here.](doc/random_type.md)

##Air Retainer
The air retainer processor will remove blocks if there used to be air in the original structure
###Format
* `processor_type`: "structure_toolkit:air_retainer"
* `to_replace`: A list of block IDs. If empty or missing, it will replace ALL blocks.
* `rarity`: The numerical chance between 0 and 1 for the block to be replaced. Defaults to 1.
* `random_type`: BLOCK, PIECE or STRUCTURE, depending on how much variety you need. Defaults to BLOCK.
###Usage
The Air retainer is used when you want naturally generated caves to intercede your structure. Ideal for structures that need to open up into the caves. For example: cavelike structures.
###Example
```json
{
  "processor_type": "structure_toolkit:air_retainer",
  "to_replace": ["minecraft:stone"],
  "rarity": 0.9,
  "random_type": "BLOCK"
}
```

#Block Mossify
A simpler version of vanilla's `minecraft:block_age`. Will only apply moss to any blocks that have a moss version. Will respect the block type, so stairs will remain stairs, positioned in the exact same way.
###Format
* `processor_type`: "structure_toolkit:block_mossify"
* `mossiness`: The numerical chance between 0 and 1 for the blocks to be replaced. Higher mossiness, results in more mossy blocks.
* `random_type`: BLOCK, PIECE or STRUCTURE, depending on how much variety you need. Defaults to BLOCK.
###Usage
Vanilla block age will also randomly introduce stairs and the likes. This can have some unwanted effects.
###Example
```json
{
  "processor_type": "structure_toolkit:block_mossify",
  "mossiness": 0.9,
  "random_type": "BLOCK"
}
```

#Gradient Replace
###Format
###Usage
###Example
```json
{
  "processor_type": "structure_toolkit:",
  "to_replace": ["minecraft:stone"],
  "rarity": 0.9,
  "random_type": "BLOCK"
}
```

#Spawner Randomizer
Allows to replace a spawner with another spawner with preset conditions. All spawner fields default to the default spawner settings.
###Format
* `delay`: Initial delay before first spawn. Defaults to 20 (roughly 1second)
* `min_spawn_delay`: Minimum delay in between spawning attempts. Defaults to 200 (roguhly 10 seconds)
* `max_spawn_delay`: Maximum delay in between spawning attempts. Defaults to 800 (roughly 40 seconds)
* `spawn_count`: Number of mobs the spawner will attempt to spawn each cycle. Defaults to 4.
* `max_nearby_entities`: Maximum number of nearby entities of the spawner's type. Defaults to 6.
* `required_player_range`: Maximum distance in blocks the player can be for the spawner to become active. Defaults to 16 blocks.  
* `spawn_range`: Maximum range for the spawner to attempt its spawns. Increase to cover a bigger area. Defaults to 4.
* `random_type`: BLOCK, PIECE or STRUCTURE, depending on how much variety you need. Defaults to BLOCK.
* `spawner_mob_entries`: A list of weighted spawns, structured as follows:
  * `resourcelocation`: The namespaced ID of the mob.
  * `weight`: A numerical weight. can be controlled to increase chance of a specific mob to appear in the spawner.
###Usage
Instead of creating 10 different rooms with 10 different spawners, create the room only once and run this processor to have a different entity inside every time it spawns.
This processor can take mobs from any mod, allowing you to add specific mobs to existing structure spawners.
###Example
```json
{
  "processor_type": "structure_toolkit:spawner_randomizer",
  "delay": 20,
  "min_spawn_delay": 200,
  "max_spawn_delay": 800,
  "spawn_count": 4,
  "max_nearby_entities": 6,
  "required_player_range": 16,
  "spawn_range": 4,
  "random_type": "BLOCK",
  "spawner_mob_entries": [
    {
      "resourcelocation": "minecraft:pig",
      "weight": 50
    }
  ]
}
```