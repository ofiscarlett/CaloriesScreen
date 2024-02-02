package com.example.caloriesscreen

import android.graphics.Color
import android.graphics.drawable.Icon
import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon as Icon1
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.caloriesscreen.ui.theme.CaloriesScreenTheme
import org.w3c.dom.Text
import androidx.compose.material3.Text as Text1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaloriesScreenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CaloriesScreen ()
                }
            }
        }
    }
}

@Composable
fun CaloriesScreen(){
    var weightInput by remember {
        mutableStateOf("")
    }
    var weight = weightInput.toIntOrNull() ?: 0
    var male by remember {
        mutableStateOf(true)
    }
    var intensity by remember {
        mutableFloatStateOf(1.3f)
    }
    var result by remember {
        mutableIntStateOf( 0)
    }
    Column(
        modifier =Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Heading(title = stringResource(R.string.calories))
        //no ageField option, should be weightField??
        //AgeField(weightInput= weightInput, onValueChange={weightInput = it})
        WeightField(weightInput= weightInput, onValueChange={weightInput = it})
        GenderChoices(male, setGenderMale ={male = it} )
        IntensityList(onClick = {intensity= it})
        Text(
            text = result.toString(),
            color = MaterialTheme.colorScheme.secondary,
            //fontSize = 20.sp, // You can adjust the value as you like
            fontWeight = FontWeight.Bold
        )

  /*      Text1(
            text = result.toString(),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = FontWeight.Bold
            )*/
        Calculation(male = male, weight = weight, intensity =intensity , setResult ={result = it} )
    }
}

//text has issue when using, suggest me to give the function
//fun Text(text: Any, color: Color, fontSize: FontWeight) {
//}

@Composable
fun Calculation(male: Boolean, weight:Int, intensity:Float, setResult:(Int)->Unit){
    Button(
        onClick = {
            if(male){
                setResult(((879 + 10.2 * weight)*intensity).toInt())
            }else{
                setResult(((795 + 7.18 * weight)*intensity).toInt())
            }
         },
        modifier= Modifier.fillMaxWidth()
    ) {
        Text(text = "CALCULATE")
    }
}

@Composable
fun Heading (title: String){
    Text1(
        text = title,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    )
}

@Composable
fun WeightField(weightInput: String, onValueChange:(String) -> Unit){
    OutlinedTextField(
        value = weightInput,
        onValueChange = onValueChange,
        label = { Text1(text = "Enter weight") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
}
@Composable
fun GenderChoices(male: Boolean, setGenderMale:(Boolean) ->Unit){
    Column(Modifier.selectableGroup()) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            RadioButton(
                selected = male,
                onClick = {setGenderMale(true)}
            )
            Text1(text = "Male")
        }
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            RadioButton(
                selected = !male,
                onClick = {setGenderMale(false)}
            )
            Text1(text = "female")
        }

    }
}
@Composable
fun IntensityList(onClick: (Float)->Unit){
    var expanded by remember { mutableStateOf( false ) }
    var selectedText by remember { mutableStateOf("light") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val items = listOf("Light", "Usual", "Moderate", "Hard", "Very Hard")
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    Column {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange ={selectedText= it},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates -> textFieldSize = coordinates.size.toSize() },
            label = { Text1( "Select intensity") },
            trailingIcon = { Icon1(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
            )
        DropdownMenu(
            expanded = expanded ,
            onDismissRequest = { expanded= false },
            modifier = Modifier
                .width(with(LocalDensity.current) {textFieldSize.width.toDp()})
            ) {
            items.forEach {label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    onClick = {
                        selectedText = label
                        var intensity = when (label) {
                            "Light" -> 1.3f
                            "Usual" -> 1.5f
                            "Moderate" -> 1.7f
                            "Hard" -> 2f
                            "Very Hard" -> 2.2f
                            else -> 0.0f
                        }
                        onClick(intensity)
                        expanded = false
                    })
                Text1(text = label)
            }
        }


    }
}

//code has issue, and command out to check again
/*
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded =false },
            modifier = Modifier
                .width(with(LocalDensity.current){
                textFieldSize.width.toDp()})
            ) {
            items.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    var intensity: Float = when (label) {
                        "Light" -> 1.3f
                        "Usual" -> 1.5f
                        "Moderate" -> 1.7f
                        "Hard" -> 2f
                        "Very Hard" -> 2.2f
                        else -> 0.0f
                    }
                    onClick(intensity)
                    expanded = false
                })
                Text1(text = label)
            }
        }
        */

//fun DropdownMenuItem(onClick: () -> Unit) = Unit
//fun DropdownMenuItem(onClick: () -> Unit) {
//}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CaloriesScreenTheme {
        CaloriesScreen()
    }
}