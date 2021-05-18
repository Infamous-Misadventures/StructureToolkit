#Decorators
Decorator processors will take something that is already there and change it. 

[Read more about Random Type here.](doc/random_type.md)

##Flower Pots
The flower pots processor will replace empty flower pots with filled ones. Any flower pot in the block tag `minecraft:flower_pot` can be selected. This allows for maximum mod compatibility.

The exclusion list can be used to exclude blocks from being selected. If your exclusion list empties the tag, that is on you.
###Format
* `processor_type`: "structure_toolkit:flower_pots"
* `includeSaplings`: Indicates whether saplings should be included. Defaults to true.
* `includeFlowers`: Indicates whether flowers should be included. Defaults to true.
* `exclusion_list`: A list of block IDs that should be excluded from the selector
* `random_type`: BLOCK, PIECE or STRUCTURE, depending on how much variety you need. Defaults to BLOCK.
###Usage
Only empty flower pots will be replaced. Build your structure with this in mind. You can have both empty and filled pots in your structure, but only the empty ones will be replaced. Ideal when you need that one specific flower to be always there.

Piece random will make sure the same flower is always used. Block random will set a separate flower for every pot.
###Example
```json
{
  "processor_type": "structure_toolkit:flower_pots",
  "includeSaplings":  true,
  "includeFlowers": true,
  "exclusion_list": ["minecraft:potted_cactus"],
  "random_type": "BLOCK"
}
```