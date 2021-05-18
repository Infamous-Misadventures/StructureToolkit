#Utility
Utility processors are mostly there to fix things or handle things.

[Read more about Random Type here.](doc/random_type.md)

##Waterlogging Fix
Code by of @TelepathicGrunt

The waterlogging fix is a fix to a bug introduced in vanilla minecraft. Waterloggable blocks in a location where water used to be will spawn in as waterlogged, even when not in the original structure.

Applying this will fix that.
###Format
* `processor_type`: "structure_toolkit:waterlogging_fix"
###Usage
Just put this as the first processor and things will be fixed.

Might have some unforeseen circumstances if you have an actual waterlogged block as well. Report any issues on the issues page.

Fingers crossed for Mojang to fix this.
###Example
```json
{
  "processor_type": "structure_toolkit:waterlogging_fix",
}
```