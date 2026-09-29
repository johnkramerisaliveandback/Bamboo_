package com.example.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.state.NavDestination
import com.example.ui.theme.BambooBg
import com.example.ui.theme.BambooBorder
import com.example.ui.theme.BambooElevated
import com.example.ui.theme.BambooPrimaryGreen
import com.example.ui.theme.BambooSoftGreen
import com.example.ui.theme.BambooTextMuted
import com.example.ui.theme.BambooTextPrimary
import com.example.ui.theme.PoppinsFontFamily

@Composable
fun FloatingNavBar(
    currentDestination: NavDestination,
    onDestinationSelected: (NavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(32.dp), ambientColor = BambooPrimaryGreen, spotColor = BambooBg)
                .clip(RoundedCornerShape(32.dp))
                .background(BambooElevated)
                .border(1.dp, BambooBorder, RoundedCornerShape(32.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavDestination.values().filter { it.isPrimary }.forEach { destination ->
                val isSelected = destination == currentDestination
                val icon = when (destination) {
                    NavDestination.HOME -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
                    NavDestination.SCHEDULE -> if (isSelected) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth
                    NavDestination.ATTENDANCE -> if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle
                    NavDestination.ACADEMICS -> if (isSelected) Icons.Filled.MenuBook else Icons.Outlined.MenuBook
                    NavDestination.MORE -> if (isSelected) Icons.Filled.Menu else Icons.Outlined.Menu
                    else -> Icons.Filled.Menu // Should not happen
                }

                FloatingNavItem(
                    destination = destination,
                    icon = icon,
                    isSelected = isSelected,
                    onClick = { onDestinationSelected(destination) }
                )
            }
        }
    }
}

@Composable
private fun FloatingNavItem(
    destination: NavDestination,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val pillBackground by animateColorAsState(
        targetValue = if (isSelected) BambooPrimaryGreen else BambooElevated,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "pillBackground"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) BambooBg else BambooTextMuted,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "iconColor"
    )

    val itemPaddingHorizontal by animateDpAsState(
        targetValue = if (isSelected) 14.dp else 10.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "itemPadding"
    )

    Box(
        modifier = Modifier
            .testTag(destination.testTag)
            .clip(CircleShape)
            .background(pillBackground)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = itemPaddingHorizontal, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = destination.title,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )

            if (isSelected) {
                Text(
                    text = destination.title,
                    color = BambooBg,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}
