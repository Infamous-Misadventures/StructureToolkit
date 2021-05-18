#Replacers
Adders are the processors that will add new blocks to your structure, usually replacing air.
They will focus on edge cases that are not possible with the vanilla `minecraft:rule`. They are also perfect when you want to replace air, but don't want to specify the air.

Some will use tags and allow for great mod compatibility, making it look like they were part of your structure all along.

[Read more about Random Type here.](doc/random_type.md)

##CeilingAttachment
Ceiling attachment's main purpose is to replace air with the given block ONLY when there is a full block above it.
###Format
* `processor_type`: "structure_toolkit:ceiling_attachment"
* `block`: The namespaced ID of the block that needs to be placed.
* `needs_wall`: Does it also require at least 1 wall to be present? Defaults to false.
* `rarity`: The numerical chance between 0 and 1 for the block to be replaced. Defaults to 1.
* `random_type`: BLOCK, PIECE or STRUCTURE, depending on how much variety you need. Defaults to BLOCK.
###Usage
Ceiling attachment allows for placing blocks to the ceiling. Examples are lanterns and cobwebs.
###Example
```json
{
  "processor_type": "structure_toolkit:ceiling_attachment",
  "block": "minecraft:cobweb",
  "needs_wall": true,
  "rarity": 0.2,
  "random_type": "BLOCK"
}
```

##Lily Pads
The Lily Pads processor will add blocks to all fluid blocks, including waterlogged blocks.
###Format
* `processor_type`: "structure_toolkit:lily_pads"
* `rarity`: The numerical chance between 0 and 1 for the appearance rate.
* `random_type`: BLOCK, PIECE or STRUCTURE, depending on how much variety you need. Defaults to BLOCK.
###Usage
You add Lily Pads...
To water surfaces...
Unsure what else could be said...

Random Type is added here as part of the Random rewrite, but it is advised to always use BLOCK for this one.
###Example
```json
{
  "processor_type": "structure_toolkit:lily_pads",
  "rarity": 0.2,
  "random_type": "BLOCK"
}
```

##Mushrooms
The mushroom processor will randomly place mushrooms. Any mushroom in the block tag `forge:mushrooms` can be selected.
The exclusion list can be used to exclude blocks from being selected. If your exclusion list empties the tag, that is on you.
###Format
* `processor_type`: "structure_toolkit:mushrooms"
* `exclusion_list`: A list of block IDs that should be excluded from the selector
* `rarity`: The numerical chance between 0 and 1 for the appearance rate.
* `random_type`: BLOCK, PIECE or STRUCTURE, depending on how much variety you need. Defaults to BLOCK.
* `mushroom_random_type`: BLOCK, PIECE or STRUCTURE, depending on how much variety you need. Defaults to PIECE.
###Usage
The mushroom random type is used to select the mushroom. This different random is necessary to allow for finegrained control. PIECE is set as the default, so that same type mushrooms always appear in groups close to eachother.

Random Type is added here as part of the Random rewrite, but it is advised to always use BLOCK for this one.
###Example
```json
{
  "processor_type": "structure_toolkit:mushrooms",
  "exclusion_list": ["minecraft:warped_fungus", "minecraft:crimson_fungus"],
  "rarity": 0.2,
  "random_type": "BLOCK"
}
```

##Snow
The snow processor will place one or more layers of snow over all blocks. 
###Format
* `processor_type`: "structure_toolkit:snow"
* `rarity`: The numerical chance between 0 and 1 for the appearance rate.
* `max_height`: The max height of the snow. Defaults to 2.
* `random_type`: BLOCK, PIECE or STRUCTURE, depending on how much variety you need. Defaults to BLOCK.
* `height_random_type`: BLOCK, PIECE or STRUCTURE, for. Defaults to BLOCK.
###Usage
Snow. Use it primarily on outside structures. A future version might introduce one that also works when your structure has indoor parts. Sadly for now this is not supported.

The height random type will allow you to have height be different for every block, or have bigger areas have the same height.

Random Type is added here as part of the Random rewrite, but it is advised to always use BLOCK for this one.
###Example
```json
{
  "processor_type": "structure_toolkit:snow",
  "rarity": 0.9,
  "max_height": 2,
  "random_type": "BLOCK",
  "height_random_type": "BLOCK"
}
```

##Vines
The vines processor will place vines on those locations where it fits. Works for both ceilings and walls, or just one of both.
###Format
* `processor_type`: "structure_toolkit:air_retainer"
* `attach_to_wall`: Is walls ok to attach vines to? Defaults to true.
* `attach_to_ceiling`: Is ceiling ok to attach vines to? Defaults to true.
* `rarity`: The numerical chance between 0 and 1 for the appearance rate.
* `random_type`: BLOCK, PIECE or STRUCTURE, depending on how much variety you need. Defaults to BLOCK.
###Usage
Ideal to give your structure a bit more age, or more jungle feeling.

Be warned that this might behave weirdly if you are also changing blocks to air. There is a risk of floating blocks.
###Example
```json
{
  "processor_type": "structure_toolkit:air_retainer",
  "attach_to_wall": true,
  "attach_to_ceiling": true,
  "rarity": 0.2,
  "random_type": "BLOCK"
}
```